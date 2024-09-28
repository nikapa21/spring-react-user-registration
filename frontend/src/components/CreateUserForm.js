import React, { useState, useContext } from "react";
import { createUser } from "../services/ApiService";
import { useNavigate } from "react-router-dom";
import { UserContext } from "../context/UserContext";

export default function CreateUserForm() {
  const navigate = useNavigate();
  const { addUser } = useContext(UserContext);

  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [email, setEmail] = useState("");
  const [error, setError] = useState(null);
  const [loading, setLoading] = useState(false);
  const [successMessage, setSuccessMessage] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSuccessMessage("");

    try {
      const newUser = { firstName, lastName, email };
      const response = await createUser(newUser);
      addUser(response);
      setSuccessMessage(
        `User ${response.firstName} has been successfully registered.`
      );
      navigate(`/${response.id}`);
    } catch (error) {
      // Handle error response from the backend
      if (error.response) {
        setError(
          error.response.data.detail ||
            "An error occurred while creating the user."
        );
      } else {
        setError("An unexpected error occurred.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
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
      {successMessage && <div style={{ color: "green" }}>{successMessage}</div>}
      <button type="submit" className="btn btn-primary" disabled={loading}>
        {loading ? "Creating..." : "Create User"}
      </button>
    </form>
  );
}
