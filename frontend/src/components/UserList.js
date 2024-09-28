import React, { useContext, useEffect } from "react";
import UserTableRow from "./UserTableRow";
import { UserContext } from "../context/UserContext";
import { getUsers } from "../services/ApiService";
import { NavLink } from "react-router-dom";

export default function UserList() {
  const { users, updateUsers } = useContext(UserContext);

  useEffect(() => {
    async function fetchData() {
      try {
        const users = await getUsers();
        updateUsers(users);
      } catch (error) {
        console.error("Error fetching users:", error);
      }
    }

    fetchData();
  }, []);

  return (
    <div>
      <nav aria-label="breadcrumb">
        <ol className="breadcrumb">
          <li className="breadcrumb-item">
            <NavLink to="/">Users</NavLink>
          </li>
        </ol>
      </nav>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">First Name</th>
            <th scope="col">Last Name</th>
            <th scope="col">Email</th>
            <th scope="col">Actions</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <UserTableRow key={user.id} {...user} />
          ))}
        </tbody>
      </table>
      <div>
        <NavLink className="btn btn-primary" to="/new">
          Add
        </NavLink>
      </div>
    </div>
  );
}
