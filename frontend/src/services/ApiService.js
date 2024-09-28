import axios from "axios";

const baseURL = process.env.REACT_APP_API_BASE_URL;

const apiUrl = `${baseURL}/api/users`;

export const getUsers = async () => {
  try {
    const response = await axios.get(apiUrl);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const createUser = async (user) => {
  try {
    const response = await axios.post(apiUrl, user);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const getUserById = async (id) => {
  try {
    const response = await axios.get(`${apiUrl}/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const updateUserById = async (id, user) => {
  try {
    const response = await axios.put(`${apiUrl}/${id}`, user);
    return response.data;
  } catch (error) {
    throw error;
  }
};

export const deleteUserById = async (id) => {
  try {
    const response = await axios.delete(`${apiUrl}/${id}`);
    return response.data;
  } catch (error) {
    throw error;
  }
};
