import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import LoginApp from './LoginApp';
import MainApp from './MainApp'
import { BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import reportWebVitals from './reportWebVitals';

const root = ReactDOM.createRoot(document.getElementById('root'));

const App = () => {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<LoginApp/>} />
                <Route path="/home" element={<MainApp/>} />
            </Routes>
        </Router>
    );
};

root.render(
    <React.StrictMode>
        <App />
    </React.StrictMode>
);

// export default App;

// const root = ReactDOM.createRoot(document.getElementById('root'));
// root.render(
//   <React.StrictMode>
//     <LoginApp />
//   </React.StrictMode>
// );

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
