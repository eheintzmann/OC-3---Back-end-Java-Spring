package com.openclassrooms.api.controller;

public class AuthentificationParams {

    public static String[][] badRequestWhenRegisterTestData() {
        // @formatter:off
        return new String[][]{

                { // Blank
                    """
                    """
                },
                { // Invalid
                    """
                    {
                    """
                },
                { // Empty body
                        """
                    {}
                    """
                },
//                { // Additional field "login"
//                        """
//                    {"email":"new@test.com","login":"test@test.com","name":"test TEST","password":"test!31"}
//                    """
//                },
                { // Existing User
                        """
                    {"email":"test@test.com","name":"test TEST","password":"test!31"}
                    """
                },
                // Email
                { // Invalid Email
                        """
                    {"email":"new","name":"test TEST","password":"test!31"}
                    """
                },
                { // Blank Email
                        """
                    {"email":"","name":"test TEST","password":"test!31"}
                    """
                },
                { // Null Email
                        """
                    {"email": null,"name":"test TEST","password":"test!31"}
                    """
                },
                { // Missing Email
                        """
                    {"name":"test TEST","password":"test!31"}
                    """
                },
                // Name
                { // Blank Name
                        """
                    {"email":"test@test.com","name":"","password":"test!31"}
                    """
                },
                { // Null Name
                        """
                    {"email":"test@test.com","name":null,"password":"test!31"}
                    """
                },
                { // Missing Name
                        """
                    {"email":"new@test.com","password":"test!31"}
                    """
                },
                // Password
                { // Blank password
                        """
                    {"email":"test@test.com","name":"test TEST","password":""}
                    """
                },
                { // Null password
                        """
                    {"email":"test@test.com","name":"test TEST","password":null}
                    """
                },
                { // Missing Password
                        """
                    {"email":"new@test.com","name":"test TEST"}
                    """
                }
        };
        // @formatter:on
    }

    public static String[][] unauthorizedWhenLoginTestData() {
        // @formatter:off
        return new String[][]{

                { // Blank
                    """
                    """
                },
//                { // Invalid
//                    """
//                    {
//                    """
//                },
                { // Empty body
                        """
                    {}
                    """
                },
//                { // Additional field "name"
//                    """
//                    {"login":"test@test.com","suppl":"test TEST","password":"test!31"}
//                    """
//                },
                { // Non Existing User
                        """
                    {"login":"none@test.com","password":"test!31"}
                    """
                },
                // Login
                { // Invalid Login
                        """
                    {"login":"test","password":"test!31"}
                    """
                },
                { // Blank Login
                        """
                    {"login":"","password":"test!31"}
                    """
                },
                { // Null Login
                        """
                    {"login":null,"password":"test!31"}
                    """
                },
                { // Missing Login
                        """
                    {"password":"test!31"}
                    """
                },
                // Password
                { // Wrong Password
                        """
                    {"login":"test@test.com","password":"test31"}
                    """
                },
                { // Blank Password
                        """
                    {"login":"test@test.com","password":""}
                    """
                },
                { // Null Password
                        """
                    {"login":"test@test.com","password":null}
                    """
                },
                { // Missing Password
                        """
                    {"login":"new@test.com"}
                    """
                }
        };
        // @formatter:on
    }
}
