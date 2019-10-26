import React from "react";
import { connect } from "react-redux";
import { login } from "../store/actions/actionCreators";
import * as actionTypes from "../store/actions/actionTypes";

const Login = ({ dispatch }) => {
    let username, password;

    return (
        <form
            onSubmit={e => {
                e.preventDefault();
                if (!username.value.trim() || !password.value.trim()) {
                    return;
                }
                dispatch(login({ username: username.value, password: password.value }))
                    .then(response => {
                        console.log("auth success: ", response.data);
                        dispatch({
                            type: actionTypes.LOGIN,
                            payload: response.data
                        });
                        localStorage.setItem("access_token", response.data.access_token);
                        window.location = "/";
                    })
                    .catch(e => console.log("login error", e));
            }}
        >
            <div className="form-group col-md-8">
                <label htmlFor="email">Email</label>
                <input type="email" id="email" className="form-control col-md-12" ref={node => (username = node)} />
            </div>
            <div className="form-group col-md-8">
                <label htmlFor="password">Password</label>
                <input type="password" id="password" className="form-control col-md-12" ref={node => (password = node)} />
            </div>
            <button type="submit" className="btn btn-primary">
                Login
            </button>
        </form>
    );
};

export default connect()(Login);
