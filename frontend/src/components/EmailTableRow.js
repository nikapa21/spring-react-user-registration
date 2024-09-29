import React from "react";

export default function EmailTableRow({ index, from, to, subject, date }) {
  return (
    <tr>
      <th scope="row">{index + 1}</th>
      <td>{`${from.Mailbox}@${from.Domain}`}</td>
      <td>
        {to.map((recipient, idx) => (
          <div key={idx}>{`${recipient.Mailbox}@${recipient.Domain}`}</div>
        ))}
      </td>
      <td>{subject}</td>
      <td>{new Date(date).toLocaleString()}</td>
    </tr>
  );
}
