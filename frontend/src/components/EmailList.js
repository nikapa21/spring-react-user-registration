import React, { useContext, useEffect } from "react";
import { NavLink } from "react-router-dom";
import { EmailContext } from "../context/EmailContext";
import { getEmails } from "../services/ApiService";
import EmailTableRow from "./EmailTableRow";

export default function EmailList() {
  const { emails, updateEmails } = useContext(EmailContext);

  useEffect(() => {
    async function fetchEmails() {
      try {
        const emails = await getEmails();
        updateEmails(emails);
      } catch (error) {
        console.error("Error fetching emails:", error);
      }
    }

    fetchEmails();
  }, []);

  return (
    <div>
      <nav aria-label="breadcrumb">
        <ol className="breadcrumb">
          <li className="breadcrumb-item">
            <NavLink to="/emails">Emails</NavLink>
          </li>
        </ol>
      </nav>
      <table className="table table-striped">
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">From</th>
            <th scope="col">To</th>
            <th scope="col">Subject</th>
            <th scope="col">Date</th>
          </tr>
        </thead>
        <tbody>
          {Array.isArray(emails) &&
            emails.map((email, index) => (
              <EmailTableRow
                key={email.ID}
                index={index}
                from={email.From}
                to={email.To}
                subject={email.Content.Headers.Subject[0]}
                date={email.Created}
              />
            ))}
        </tbody>
      </table>
    </div>
  );
}
