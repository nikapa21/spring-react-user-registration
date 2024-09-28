import React, { useContext, useEffect, useState } from "react";
import { getUserById, updateUserById } from "../services/ApiService";
import { NavLink, useNavigate, useParams } from "react-router-dom";
import { UserContext } from "../context/UserContext";

export default function UpdateUserForm() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { user, updateUser } = useContext(UserContext);

  // Local state for form fields and error handling
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function fetchData() {
      try {
        const fetchedUser = await getUserById(id);
        setFirstName(fetchedUser.firstName);
        setLastName(fetchedUser.lastName);
        setEmail(fetchedUser.email);
        updateUser(fetchedUser);
      } catch (error) {
        console.error("Error fetching user:", error);
        setError("Failed to fetch user data.");
      }
    }

    fetchData();
  }, [id]);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const updatedUser = {
        id: id,
        firstName,
        lastName,
        email,
      };

      const response = await updateUserById(id, updatedUser);
      updateUser(response);
      navigate(`/${response.id}`);
    } catch (error) {
      console.error("Error updating user:", error);
      setError(
        error.response?.data?.detail ||
          "An error occurred while updating the user."
      );
    } finally {
      setLoading(false);
    }
  };

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
          <li className="breadcrumb-item active">
            <NavLink active to={`/${id}/edit`}>
              Edit
            </NavLink>
          </li>
        </ol>
      </nav>
      <form onSubmit={handleSubmit}>
        <div className="mb-3 mt-5">
          <label htmlFor="firstName" className="form-label">
            First Name
          </label>
          <input
            type="text"
            className="form-control"
            id="firstName"
            value={firstName}
            onChange={(e) => setFirstName(e.target.value)}
            required
          />
          <div id="firstNameHelp" className="form-text">
            Input the user's first name here.
          </div>
        </div>
        <div className="mb-3">
          <div className="row">
            <div className="col-6">
              <label htmlFor="lastName" className="form-label">
                Last Name
              </label>
              <input
                type="text"
                className="form-control"
                id="lastName"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                required
              />
            </div>
            <div className="col-6">
              <label htmlFor="email" className="form-label">
                Email
              </label>
              <input
                type="email"
                className="form-control"
                id="email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                required
              />
            </div>
          </div>
        </div>
        {error && <div style={{ color: "red" }}>{error}</div>}
        <button type="submit" className="btn btn-primary" disabled={loading}>
          {loading ? "Updating..." : "Update User"}
        </button>
      </form>
    </div>
  );
}
