import React, { useContext, useState } from "react";
import { NavLink } from "react-router-dom";
import {
  deactivateUserById,
  activateUserById,
  getUsers,
} from "../services/ApiService";
import { UserContext } from "../context/UserContext";

export default function UserTableRow({
  id,
  firstName,
  lastName,
  email,
  isDeactivated,
}) {
  const { updateUsers } = useContext(UserContext);

  async function deactivateUser() {
    try {
      await deactivateUserById(id);
      const updatedUsers = await getUsers();
      updateUsers(updatedUsers);
    } catch (error) {
      console.error("Error deactivating user:", error);
    }
  }

  async function activateUser() {
    try {
      await activateUserById(id);
      const updatedUsers = await getUsers();
      updateUsers(updatedUsers);
    } catch (error) {
      console.error("Error activating user:", error);
    }
  }

  return (
    <tr style={{ opacity: isDeactivated ? 0.5 : 1 }}>
      <th scope="row">{id}</th>
      <td>{firstName}</td>
      <td>{lastName}</td>
      <td>{email}</td>
      <td>{isDeactivated ? "Inactive" : "Active"}</td>
      <td>
        <div className="btn-group">
          <NavLink
            className="btn btn-info"
            to={`/${id}`}
            disabled={isDeactivated}
          >
            View
          </NavLink>
          <NavLink
            className="btn btn-light"
            to={`/${id}/edit`}
            disabled={isDeactivated}
          >
            Edit
          </NavLink>
          {isDeactivated ? (
            <button onClick={activateUser} className="btn btn-success">
              Activate
            </button>
          ) : (
            <button onClick={deactivateUser} className="btn btn-danger">
              Deactivate
            </button>
          )}
        </div>
      </td>
    </tr>
  );
}
