import React, { useState, useEffect} from 'react';

const LogIn = () => {
    return (
        <div className="w-64 flex shadow-lg flex-col bg-cover bg-center justify-content bg-white p-6 rounded pt-8 pb-8">
            <div className="text-center text-gray-500 mb-6">
                <h2>SIGN UP</h2>
            </div>
            <div>
                <form>
                    <input
                        className="bg-transparent border-b m-auto block border-gray-500 w-full mb-6 text-gray-500 pb-1"
                        type="text"
                        placeholder="Username"/>
                    <input
                        className="bg-transparent border-b m-auto block border-gray-500 w-full mb-6 text-gray-500 pb-1"
                        type="password"
                        placeholder="password"/>
                    <div className="flex mt-4">
                        <input type="checkbox"
                               className="mr-2"
                               name="agreement"
                               value="agree"/>
                        <p className="text-grey">Accept the
                            <a href="#"
                               className=" no-underline text-teal-500 hover:text-teal-400">Terms and
                                Conditions
                            </a>
                        </p>
                    </div>
                    <input
                        className="shadow-lg pt-3 pb-3 mt-6 w-full text-white bg-teal-500 hover:bg-teal-400 rounded-full "
                        type="submit"
                        value="SIGN IN"/>
                </form>
            </div>
            <div>
                <p className="mt-4 text-gray-500 text-sm">Have an account?
                    <a href="#"
                       className="no-underline text-teal-500 hover:text-teal-400">Log in
                    </a>
                </p>
            </div>
        </div>
    );
};


export default LogIn;