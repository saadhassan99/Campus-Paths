/*
 * Copyright (C) 2020 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Autumn Quarter 2020 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import Map from './Map'
import "./App.css"
import Header from "./Header";
import Form from "./Form";

interface AppState {
    importantData: {cost: 0, path: [], start:{}}
}

class App extends Component<{}, AppState> {

    constructor(props: any) {
        super(props);
        this.state = {
            importantData: {cost: 0, path: [], start:{}}
        }
    }

    onChangeLinkName = (newName: {cost: 0, path: [], start:{}}) => {
        this.setState({
            importantData: newName
        })
    }

    render() {
        return (
            // app will have a header (a pic) and an app body containing the
            // search boxes and the find button on the left side and the map on
            // the right side.

            <div className={"app"}>
                <Header/>
                <div className={"app__body"}>
                    <Form onChange={this.onChangeLinkName}/>
                    <Map coordinates={this.state.importantData}/>
                </div>
            </div>
        );
    }

}

export default App;
