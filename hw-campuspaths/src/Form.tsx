import React, {Component} from "react";

interface FormProps {
    onChange(newData: {}): void;  // called when a new size is picked
}

class Form extends Component<FormProps> {

    state = {
        startingPosition: '',
        destination: '',
        url: '',
        importantData: {cost: 0, path: [], start:{}} // parsed data in to JSON file that was returned by the server
    }

    onInputChange = (e: any) =>{
        this.setState({
            [e.target.name]: e.target.value
        })
    }

    // onSubmit button linked to 'Find Route' that will request the server for the route
    // returns: JSON file with that contain the route
    onSubmit = (e: any) => {
        e.preventDefault()
        this.state.url = `http://localhost:4567/findroute?start=${this.state.startingPosition}&dest=${this.state.destination}`
        console.log("url: ", this.state.url)
        this.sendRequest().then(() => {
            this.props.onChange(this.state.importantData);
        })
    }

    // clear all outputs and drawings on the map
    onClear() {
        let buttonClear = document.querySelector('button')
        let inputs = document.querySelectorAll('input')
        // @ts-ignore
        buttonClear.addEventListener('click', () => {
            inputs.forEach(input => input.value = '')
        })
    }

    // request the server for the route.
    async sendRequest() {
        try {
            let responsePromise = fetch(this.state.url)
            let response = await responsePromise
            let parsingPromise = response.json()
            let parsedObject = await parsingPromise
            this.setState({
                importantData: parsedObject
            })
        } catch (e) {
            //alert("Error!" + e)
            alert("Enter short name of the building\n\nE.g CSE, KNE, EEB (S) etc.")
        }
    }

    render() {
        return (

            <form className={"searchForm"}>
                <div className={"formBox"}>

                    <input
                        type={"text"}
                        name={"startingPosition"}
                        id={"startingPosition"}
                        value={this.state.startingPosition}
                        onChange={event => this.onInputChange(event)}
                        placeholder={"enter starting point"}
                    />
                </div>
                <div className={"formBox"}>
                    <input
                        type={"text"}
                        name={"destination"}
                        id={"destination"}
                        value={this.state.destination}
                        onChange={event => this.onInputChange(event)}
                        placeholder={"enter destination"}
                    />
                </div>
                <div className={"formBox"}>
                    <button onClick={e => this.onSubmit(e)}>Find Route</button>
                    <button onClick={this.onClear}>clear</button>
                </div>
            </form>

        )
    }
}

export default Form;