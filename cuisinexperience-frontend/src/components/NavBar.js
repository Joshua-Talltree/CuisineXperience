import React from 'react';
import { NavLink } from "react-router-dom";
import { SocialIcon } from "react-social-icons";
import Search from '../components/Search'

export default function NavBar() {
    return (
        <header className="bg-yellow-50">
            <div className="container mx-auto flex justify-between">
                <nav className="flex">
                    <NavLink
                        to="/"
                        exact
                        activeClassName="text-white"
                        className="inflex-flex items-center py-6 px-3 mr-4 text-blue-100 hover:text-green-800 text-4xl font-bold tracking-widest"
                    >
                        Joshua Talltree
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
                <div className="inline-flex py-1 px-1 my-6">
                    <Search
                        className="mr-4"
                        target="_blank"
                        fgColor="#fff"
                        style={{ height: 20, width: 20 }}
                    />
                    <SocialIcon
                        url="https://www.reddit.com/user/Good_Historian3282"
                        className="mr-4"
                        target="_blank"
                        fgColor="#fff"
                        style={{ height: 35, width: 35 }}
                    />
                </div>
            </div>
        </header>
    );
}