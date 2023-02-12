package pl.coderslab.workshop2.UserDBManagement;

public class MainDAO {
    public static void main(String[] args) {

        UserDao userDao = new UserDao();
        User user = new User();

        user.setName("Programista");
        user.setPassword("duży secret");
        user.setEmail("Programista@coderslab.pl");
//
        userDao.create(user);

//        User userToUpdate = userDao.read(21);
//
//        userToUpdate.setName("Arkadiusz");
//        userToUpdate.setEmail("arek@coderslab.pl");
//        userToUpdate.setPassword("superPassword");
//
//        userDao.update(userToUpdate);

        User[] all = userDao.findAll();
        for (User u : all) {
            System.out.println(u);
        }
        userDao.delete(23);
        all = userDao.findAll();
        for (User u : all) {
            System.out.println(u);
        }
    }
}
