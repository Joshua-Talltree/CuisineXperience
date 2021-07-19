import React, {useState, useEffect} from "react";
import axios from 'axios';


const Search = () => {
    const [term, setTerm] = useState('programming');
    const [debouncedTerm, setDebouncedTerm] = useState('programming');
    const [results, setResults] = useState([]);

    useEffect(() => {
        const timerId = setTimeout(() => {
            setDebouncedTerm(term);
        }, 1000);

        return () => {
            clearTimeout(timerId);
        };
    }, [term]);

    useEffect(() => {
        const search = async () => {
            const {data} = await axios.get('https://en.wikipedia.org/w/api.php', {
                params: {
                    action: 'query',
                    list: 'search',
                    origin: '*',
                    format: 'json',
                    srsearch: debouncedTerm,
                }
            });

            setResults(data.query.search);
        };
        search();
    }, [debouncedTerm]);


    const renderedResults = results.map((result) => {
        return (
            <div key={result.pageid} className="item">
                <div className="right floated content">
                    <a
                        className="ui button"
                        href={`https://en.wikipedia.org?curid=${result.pageid}`}
                    >
                        Go
                    </a>
                </div>
                <div className="content">
                    <div className="header">
                        {result.title}
                    </div>
                    {/*this has security issues*/}
                    <span dangerouslySetInnerHTML={{__html: result.snippet}}/>
                </div>
            </div>
        );
    });


    return (
        <div className="p-1">
            <div className="bg-white flex items-center rounded-full shadow-xl">
                <input className="input rounded-l-full w-full py-4 px-6 text-gray-700 leading-tight focus:outline-none"
                       value={term}
                       onChange={e => setTerm(e.target.value)}
                />
                <div className="p-4">
                    <button className="bg-blue-500 text-white rounded-full p-2 hover:bg-blue-400 focus:outline-none w-12 h-12 flex items-center justify-center">
                        icon
                    </button>
                </div>
            </div>
        </div>
    );
};



export default Search;