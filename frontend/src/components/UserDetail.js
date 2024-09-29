import React, { useEffect, useContext } from "react";
import { NavLink, useParams, useNavigate } from "react-router-dom";
import { getUserById } from "../services/ApiService";
import { UserContext } from "../context/UserContext";

export default function UserDetail() {
  const { id } = useParams();
  const { user, updateUser } = useContext(UserContext);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchData() {
      try {
        const user = await getUserById(id);
        updateUser(user);
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
          <li className="breadcrumb-item">
            <NavLink active to={`/${id}`}>
              {id}
            </NavLink>
          </li>
        </ol>
      </nav>
      <h4 className="text-center mb-5 mt-5">User Info: {id}</h4>
      <table className="table">
        <tbody>
          <tr>
            <th scope="row">First Name</th>
            <td>{user.firstName}</td>
          </tr>
          <tr>
            <th scope="row">Last Name</th>
            <td>{user.lastName}</td>
          </tr>
          <tr>
            <th scope="row">Email</th>
            <td>{user.email}</td>
          </tr>
        </tbody>
      </table>
      <div>
        <div className="row">
          <div className="col-6">
            <NavLink className="btn btn-light" to={`/${id}/edit`}>
              Edit
            </NavLink>
          </div>
        </div>
      </div>
    </div>
  );
}
