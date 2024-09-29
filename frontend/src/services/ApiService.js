import axios from "axios";

const baseURL = process.env.REACT_APP_API_BASE_URL;

const usersApiUrl = `${baseURL}/api/users`;
const emailsApiUrl = `${baseURL}/api/emails`;

export const getUsers = async () => {
  try {
    const response = await axios.get(usersApiUrl);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const createUser = async (user) => {
  try {
    const response = await axios.post(usersApiUrl, user);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getUserById = async (id) => {
  try {
    const response = await axios.get(`${usersApiUrl}/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const updateUserById = async (id, user) => {
  try {
    const response = await axios.put(`${usersApiUrl}/${id}`, user);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const deactivateUserById = async (id) => {
  try {
    await axios.put(`${usersApiUrl}/${id}/deactivate`);
  } catch (error) {
    throw error;
  }
};

export const activateUserById = async (id) => {
  try {
    await axios.put(`${usersApiUrl}/${id}/activate`);
  } catch (error) {
    throw error;
  }
};

export const getEmails = async () => {
  try {
    const response = await axios.get(emailsApiUrl);
    return response.data.items;
  } catch (error) {
    throw error;
  }
};
