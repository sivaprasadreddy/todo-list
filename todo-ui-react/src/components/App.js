import React from "react";
import { Route, Switch } from "react-router-dom";
import Home from "./Home";
import NavBar from "./NavBar";

const App = () => (
    <div className="App">
        <NavBar />
        <main role="main" className="container">
            <Switch>
                <Route exact path="/" component={Home} />
                <Route component={Home} />
            </Switch>
        </main>
    </div>
);

export default App;
