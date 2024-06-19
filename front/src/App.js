import React from 'react';
import './App.css';
import Game from './components/Game';

function App() {
    return (
        <div className="App">
            <header className="App-header">
                <h1>World of Zuul</h1>
                <Game />
            </header>
        </div>
    );
}

export default App;
