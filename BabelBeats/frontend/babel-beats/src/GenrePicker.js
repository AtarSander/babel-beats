import React from 'react';
import "./GenrePicker.css";
import { ReactPhotoCollage } from "react-photo-collage";

function processImages(imgList) {
    let layout;
    let height = ['150px', '150px'];
    let photos = imgList.map((img) => ({
        source: img,
    }));
    photos = photos.length > 4 ? photos.slice(0, 4) : photos;

    if(photos.length >= 3)
        layout = [photos.length - 2, 2];

    else {
        height = ['300px']
        layout = [photos.length];
    }

    return {
        width: '300px',
        height: height,
        layout: layout,
        photos: photos,
        showNumOfRemainingPhotos: true,
    };
}

function GenrePicker({ genreImagesJSON }) {
    let genres = genreImagesJSON.map(element => element.genre)
    let imgSettings = genreImagesJSON.map(element => processImages(element.artistIcon));
    console.log(genres);

    return (
        <>
            <h1 className={"chooseGenreTitle"}>Choose a genre:</h1>
            <div className={"genreList"}>
                {imgSettings.map((setting, index) => (
                    <div key={index} className={"genreListElement"}>
                        <ReactPhotoCollage {...setting}/>
                        <br />
                        {genres[index]}
                    </div>
                ))}
            </div>
        </>
    );
}

export default GenrePicker;