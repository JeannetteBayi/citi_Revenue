/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mobitill.citirevenue.ejb;

import com.mobitill.citirevenue.entity.Role;
import com.mobitill.citirevenue.entity.User;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 *
 * @author Francis
 */
@Stateless
public class AccesSessionBean {

    @PersistenceContext(unitName = "com.mobitill_CitiRevenue_war_1PU")
    private EntityManager em;
    private final String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
    private static final Logger LOG = Logger.getLogger(AccesSessionBean.class.getName());

    @EJB
    private UtilitySessionBean usb;
    @EJB
    private MailSessionBean mail;
    @Resource
    private EJBContext context;

    public boolean accessRequest(String appac, String role) {
        if (usb.trimedString(appac)) {
            try {
                User user = em.createNamedQuery("User.findByHashvalue", User.class)
                        .setParameter("hashvalue", appac.toUpperCase())
                        .getSingleResult();
                if (user.getRoleList() != null) {
                    if (user.getRoleList().size() > 0) {
                        String userRole = user.getRoleList().get(0).getRolename();
                        if (userRole.equalsIgnoreCase("MANAGEMENT")) {
                            return true;
                        } else {
                            return role.equals(user.getRoleList().get(0).getRolename());
                        }
                    }
                }
            } catch (Exception e) {
                LOG.log(Level.WARNING, String.format("accessRequest invalid user [%s]", appac));
                return false;
            }
        } else {
            LOG.log(Level.WARNING, String.format("accessRequest invalid user [%s]", appac));
        }
        return false;
    }

    public boolean accessRequest(String appac) {
        if (usb.trimedString(appac)) {
            try {
                User user = em.createNamedQuery("User.findByHashvalue", User.class)
                        .setParameter("hashvalue", appac.toUpperCase())
                        .getSingleResult();
                List<String> roles = new ArrayList<>();
                List<Role> roleList = user.getRoleList();
                for (Role role : roleList) {
                    roles.add(role.getRolename().toUpperCase());
                }
                return true;
            } catch (Exception e) {
                LOG.log(Level.WARNING, String.format("accessRequest invalid user [%s]", appac));
                return false;
            }
        } else {
            LOG.log(Level.WARNING, String.format("accessRequest invalid user [%s]", appac));
        }
        return false;
    }

    public Map<String, String> requestLogin(String appac) {
        Map<String, String> map;
        if (usb.trimedString(appac)) {
            try {
                User user = em.createNamedQuery("User.findByHashvalue", User.class)
                        .setParameter("hashvalue", appac.toUpperCase())
                        .getSingleResult();
                List<String> roles = new ArrayList<>();
                List<Role> roleList = user.getRoleList();
                for (Role role : roleList) {
                    roles.add(role.getRolename().toUpperCase());
                }
                map = new LinkedHashMap<>();
                map.put("username", user.getUsername());
                map.put("role", roles.get(0));
                LOG.log(Level.INFO, "pin {0}", user.getPin());
                if (user.getPin() != null) {
                    map.put("pin", user.getPin());
                }
                return map;
            } catch (Exception e) {
                LOG.log(Level.WARNING, String.format("requestLogin invalid user [%s]", appac));
                return null;
            }
        } else {
            LOG.log(Level.WARNING, String.format("requestLogin invalid user [%s]", appac));
        }
        return null;
    }

    public Object createUser(User user) {
        if (user == null) {
            return "missing information";
        }
        try {
            Boolean usernameExist = em.createNamedQuery("User.findByUsername", Boolean.class)
                    .setParameter("username", user.getUsername())
                    .getSingleResult();
            if (usernameExist) {
                return "username already exist";
            }
            if (usb.trimedString(user.getPassword())) {
                if (!user.getPassword().matches(pattern)) {
                    return "weak password";
                }
            } else {
                return "field password cannot be empty";
            }
            if (!user.getRole().equalsIgnoreCase("MANAGEMENT") && !user.getRole().equalsIgnoreCase("VIEW")) {
                return "role allowed options MANAGEMENT or VIEW";
            }

            StringBuilder sb = generatePasswordHash(user.getUsername(), user.getPassword());
            user.setHashedPassword(sb.toString());

            Role role = new Role();
            role.setAppuser(user);
            role.setRolename(user.getRole().toUpperCase());
            role.setUsername(user.getUsername());

            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoleList(roles);

            em.persist(user);
            mail.shareLoginCredentials(user.getUsername(), user.getPassword(), user.getEmail());
            user.setPassword("");

            return user;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }

    public User autoCreateUser(String username, String pswd, String pin, String email) {
        User user = new User();
        try {
            String password;

            if (pswd == null) {
                password = RandomStringUtils.randomAlphanumeric(10);
                while (!password.matches(pattern)) {
                    LOG.log(Level.WARNING, "weak password");
                    password = RandomStringUtils.randomAlphanumeric(10);
                }
            } else {
                password = pswd;
            }
            while (em.createNamedQuery("User.findByUsername", Boolean.class)
                    .setParameter("username", username)
                    .getSingleResult()) {
                LOG.log(Level.WARNING, "duplicate username ");
                username = RandomStringUtils.randomAlphabetic(8);
            }

            StringBuilder sb = generatePasswordHash(username, password);

            user.setHashedPassword(sb.toString());
            user.setUsername(username);
            user.setEmail(email);
            user.setRole("VIEW");
            user.setPin(pin);

            Role role = new Role();
            role.setAppuser(user);
            role.setRolename(user.getRole().toUpperCase());
            role.setUsername(user.getUsername());

            List<Role> roles = new ArrayList<>();
            roles.add(role);
            user.setRoleList(roles);

            user.setPassword(password);
            em.persist(user);

            mail.shareLoginCredentials(username, password, email);

            return user;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return null;
        }
    }

    public StringBuilder generatePasswordHash(String username, String password) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        String encoded = Base64.getEncoder().encodeToString(hash);
        sb.append(encoded);
        return sb;
    }

    public Object editUser(User user) {
        if (user == null) {
            return "missing information";
        }
        try {
            User editUser;
            try {
                editUser = em.createNamedQuery("User.findUsername", User.class)
                        .setParameter("username", user.getUsername())
                        .getSingleResult();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "invalid user");
                return "invalid user";
            }

            if (usb.trimedString(user.getPassword())) {
                if (!user.getPassword().matches(pattern)) {
                    return "weak password";
                }
            } else {
                return "field password cannot be empty";
            }

            StringBuilder sb = generatePasswordHash(user.getUsername(), user.getPassword());
            editUser.setHashedPassword(sb.toString());
            editUser.setPassword(user.getPassword());

            return user;

        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            context.setRollbackOnly();
            return "something went wrong";
        }
    }

    public List<User> getUsers() {
        List<User> resultList = em.createNamedQuery("User.findAll", User.class).getResultList();
        StringBuilder sb = new StringBuilder();
        for (User user : resultList) {
            user.setRole("");
            user.setPassword("");
            if (user.getRoleList() != null) {
                sb.setLength(0);
                for (Role role : user.getRoleList()) {
                    sb.append(role.getRolename()).append(",");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    user.setRole(sb.toString());
                }
            } else {
            }
        }
        return resultList;
    }

    public String getPin(String username) {
        try {
            return em.createNamedQuery("User.findPinByUsername", String.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public List<String> getRoles(String username) {
        try {
            return em.createNamedQuery("Role.getRoles", String.class)
                    .setParameter("username", username)
                    .getResultList();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public Object changePassword(User user, String oldPassword) {
        if (user == null) {
            return "missing information";
        }
        try {
            if (usb.trimedString(user.getPassword())) {
                if (!user.getPassword().matches(pattern)) {
                    return "weak password";
                }
            } else {
                return "field password cannot be empty";
            }

            User editUser;
            try {
                editUser = em.createNamedQuery("User.findByUsernameHashValue", User.class)
                        .setParameter("username", user.getUsername())
                        .setParameter("passhash", generatePasswordHash(user.getUsername(), oldPassword).toString())
                        .getSingleResult();
            } catch (Exception e) {
                LOG.log(Level.WARNING, "invalid user");
                return "invalid user";
            }

            StringBuilder sb = generatePasswordHash(user.getUsername(), user.getPassword());
            editUser.setHashedPassword(sb.toString());
            editUser.setRole("dummy role");
            editUser.setPassword("dummy holder");

            return user;
            //ConstraintViolationException
        } catch (ConstraintViolationException e) {
            LOG.log(Level.SEVERE, e.getMessage());
            return "something went wrong";
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return "something went wrong";
        }
    }

    public Object resetPassword(User user) {
        if (user == null) {
            return "missing information";
        }
        try {
            if (!usb.trimedString(user.getEmail())) {
                return "missing email address field";
            }
            String password = RandomStringUtils.randomAlphanumeric(10);
            while (!password.matches(pattern)) {
                LOG.log(Level.WARNING, "weak password");
                password = RandomStringUtils.randomAlphanumeric(10);
            }
            user.setPassword(password);
            StringBuilder sb = generatePasswordHash(user.getUsername(), user.getPassword());
            user.setHashedPassword(sb.toString());
            
            em.merge(user);
            mail.shareResetLoginCredentials(user.getUsername(), user.getPassword(), user.getEmail());
            return user;
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return "something went wrong";
        }
    }
}
