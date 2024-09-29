import React, { createContext, useState } from "react";

// Create the context
export const EmailContext = createContext();

export const EmailProvider = ({ children }) => {
  const [emails, setEmails] = useState([]);

  const updateEmails = (newEmails) => {
    setEmails(newEmails);
  };

  return (
    <EmailContext.Provider value={{ emails, updateEmails }}>
      {children}
    </EmailContext.Provider>
  );
};
