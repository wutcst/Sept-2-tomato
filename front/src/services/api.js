import axios from 'axios';

const API_URL = 'http://localhost:5000/api'; // 后端API的URL

export const getGameState = async () => {
    const response = await axios.get(`${API_URL}/game-state`);
    return response.data;
};

export const movePlayer = async (direction) => {
    const response = await axios.post(`${API_URL}/move`, { direction });
    return response.data;
};

export const takeItem = async (itemName) => {
    const response = await axios.post(`${API_URL}/take`, { itemName });
    return response.data;
};

export const dropItem = async (itemName) => {
    const response = await axios.post(`${API_URL}/drop`, { itemName });
    return response.data;
};
