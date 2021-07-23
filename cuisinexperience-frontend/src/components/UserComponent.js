import React, {useState, useEffect} from 'react';
import axios from 'axios';


const UserComponent = () => {
    const [users, getUsers] = useState('');
    const [results, setResults] = useState([]);

    useEffect(() => {
        const users_api = async () => {
            const {data} = await axios.get('http://localhost:8080/api/users');

            setResults(data.query.users_api);
        };

        users_api();
    }, [users])


    const renderedResults = results.map((result) => {
        return (
            <div>
                <h1 className="text-center"> Users List</h1>
                <table className="table table-striped">
                    <thead>
                    <tr>

                        <td> {result.id}</td>
                        <td> {result.username}</td>
                        <td> {result.email}</td>
                    </tr>
                    {renderedResults}
                    </thead>
                    <tbody>
                    {

                    }
                    </tbody>
                </table>

            </div>
        )
    });
}

export default UserComponent;