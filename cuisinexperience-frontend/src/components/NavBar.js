import React from 'react';
import { NavLink } from "react-router-dom";
import Icon from 'react-icons-kit';
import { login } from 'react-icons-kit/iconic/login'
import Search from '../components/Search'


export default function NavBar() {
    return (
        <header className="bg-gray-200">
            <div className="container mx-auto flex justify-between">
                <nav className="flex">
                    <NavLink
                        url="./src/logo.png"
                        to="/"
                        exact
                        activeClassName="text-white"
                        className="inflex-flex items-center py-6 px-3 mr-4 text-blue-100 hover:text-green-800 text-4xl font-bold tracking-widest"
                    >
                        CuisineXperience
                    </NavLink>
                    <NavLink
                        to="/post"
                        className="inline-flex py-3 px-3 my-6 rounded text-blue-200 hover:text-green-800"
                        activeClassName="text-blue-100 bg-blue-700"
                    >
                        Blog Posts
                    </NavLink>
                    <NavLink
                        to="/project"
                        className="inline-flex py-3 px-3 my-6 rounded text-blue-200 hover:text-green-800"
                        activeClassName="text-blue-100 bg-blue-700"
                    >
                        Projects
                    </NavLink>
                    <NavLink
                        to="/about"
                        className="inline-flex py-3 px-3 my-6 rounded text-blue-200 hover:text-green-800"
                        activeClassName="text-blue-100 bg-blue-700"
                    >
                        About Me!
                    </NavLink>
                </nav>
                <div className="inline-flex py-2 px-4 my-6">
                    <Search
                        className="mr-4"
                        target="_blank"
                        fgColor="#fff"
                        style={{ height: 20, width: 20 }}
                    />
                    <Icon
                        className="mr-4 px-2"
                        target="_blank"
                        title="login"
                        icon={login}
                        size={26}
                    />
                </div>
            </div>
        </header>
    );
}