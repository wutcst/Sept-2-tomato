import React, { useContext, useEffect } from 'react';
import { GameContext } from '../context/GameContext';
import Room from './Room';
import Player from './Player';
import socket from '../services/socket';

const Game = () => {
    const { player, currentRoom, setPlayer, setCurrentRoom } = useContext(GameContext);

    useEffect(() => {
        socket.on('connect', () => {
            console.log('Connected to server');
        });

        socket.on('gameState', (data) => {
            setPlayer(data.player);
            setCurrentRoom(data.currentRoom);
        });

        return () => {
            socket.off('connect');
            socket.off('gameState');
        };
    }, [setPlayer, setCurrentRoom]);

    return (
        <div>
            <h1>World of Zuul</h1>
            {player && currentRoom ? (
                <div>
                    <Room room={currentRoom} />
                    <Player player={player} />
                </div>
            ) : (
                <p>Loading game...</p>
            )}
        </div>
    );
};

export default Game;
