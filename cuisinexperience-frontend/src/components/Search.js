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
        <div className="px-3">
            <div className="flex border-grey-light border">
                <input className="w-full rounded ml-4"
                       type="text"
                       placeholder="Search..."
                       value={term}
                       onChange={e => setTerm(e.target.value)}
                />
                <button className="bg-grey-lightest border-grey border-l shadow hover:bg-grey-lightest">
                    <span className="w-auto flex justify-end items-center text-grey p-1 hover:text-grey-darkest">
                    <i className="material-icons text-xs">search</i>
                    </span>
                </button>
            </div>
        </div>
    );
};


export default Search;