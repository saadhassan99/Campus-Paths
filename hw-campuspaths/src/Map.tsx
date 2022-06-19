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
import "./Map.css";

interface MapProps {
    coordinates: {cost: 0, path: [], start:{}}
}

interface MapState {
    backgroundImage: HTMLImageElement | null;
}

class Map extends Component<MapProps, MapState> {

    // NOTE:
    // This component is a suggestion for you to use, if you would like to.
    // It has some skeleton code that helps set up some of the more difficult parts
    // of getting <canvas> elements to display nicely with large images.
    //
    // If you don't want to use this component, you're free to delete it.

    canvas: React.RefObject<HTMLCanvasElement>;

    constructor(props: MapProps) {
        super(props);
        this.state = {
            backgroundImage: null,
        };
        this.canvas = React.createRef();
    }

    componentDidMount() {
        // Might want to do something here?
        this.fetchAndSaveImage();
        this.drawBackgroundImage();
    }

    componentDidUpdate() {
        // Might want something here too...
        this.drawBackgroundImage();
    }

    fetchAndSaveImage() {
        // Creates an Image object, and sets a callback function
        // for when the image is done loading (it might take a while).
        let background: HTMLImageElement = new Image();
        background.onload = () => {
            this.setState({
                backgroundImage: background
            });
        };
        // Once our callback is set up, we tell the image what file it should
        // load from. This also triggers the loading process.
        background.src = "./campus_map.jpg";
    }


    drawBackgroundImage() {

        let canvas = this.canvas.current;
        if (canvas === null) throw Error("Unable to draw, no canvas ref.");
        let ctx = canvas.getContext("2d");
        if (ctx === null) throw Error("Unable to draw, no valid graphics context.");
        //
        if (this.state.backgroundImage !== null) { // This means the image has been loaded.
            // Sets the internal "drawing space" of the canvas to have the correct size.
            // This helps the canvas not be blurry.
            canvas.width = this.state.backgroundImage.width;
            canvas.height = this.state.backgroundImage.height;
            ctx.drawImage(this.state.backgroundImage, 0, 0);

        }

        // Draw the path
        const pathCoordinates = this.getCoordinates()

        for (let pathCoordinate of pathCoordinates) {
            this.drawPath(ctx, pathCoordinate);
        }

    }

    // You could write CanvasRenderingContext2D as the type for ctx, if you wanted.
    drawPath = (ctx: any , coordinate: [number, number, number, number]) => {

        ctx.fillStyle = "red";
        // Generally use a radius of 4, but when there are lots of dots on the grid (> 50)
        // we slowly scale the radius down so they'll all fit next to each other.
        ctx.beginPath();
        ctx.lineWidth = 15;
        ctx.strokeStyle = "#ff0000"
        ctx.moveTo(coordinate[0], coordinate[1]);
        ctx.lineTo(coordinate[2], coordinate[3]);

        ctx.stroke()
        ctx.fill()

    };

    getCoordinates = (): [number, number, number, number][] => {

        let path = this.props.coordinates.path

        // @ts-ignore
        return path.map(cur => [cur.start.x, cur.start.y, cur.end.x, cur.end.y]);

    }

    render() {
        return (
            <canvas ref={this.canvas}/>
        )
    }
}

export default Map;