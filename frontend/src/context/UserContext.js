import React, { createContext, useState } from "react";

export const UserContext = createContext();

export const UserListProvider = ({ children }) => {
  const [users, setUsers] = useState([]);
  const [user, setUser] = useState({});

  const updateUsers = (users) => {
    setUsers(users);
  };

  const addUser = (user) => {
    setUsers((prevUsers) => [...prevUsers, user]);
  };

  const updateUser = (user) => {
    setUser(user);
  };

  const activateUserById = (updatedUser) => {
    setUsers((prevUsers) =>
      prevUsers.map((user) => (user.id === updatedUser.id ? updatedUser : user))
    );
  };

  return (
    <UserContext.Provider
      value={{
        users,
        user,
        updateUsers,
        updateUser,
        addUser,
        activateUserById,
      }}
    >
      {children}
    </UserContext.Provider>
  );
};
