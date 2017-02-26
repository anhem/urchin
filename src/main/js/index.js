/* global module */
import {AppContainer} from "react-hot-loader";
import React from "react";
import ReactDOM from "react-dom";
import App from "./App";

const rootEl = document.getElementById('app');
ReactDOM.render(
    <AppContainer>
        <App />
    </AppContainer>,
    rootEl
);
if (module.hot) module.hot.accept('./App', () => {
    ReactDOM.render(
        <AppContainer>
            <App />
        </AppContainer>,
        rootEl
    )
});