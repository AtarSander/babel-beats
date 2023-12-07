function readFromFile(filePath) {
    let filePath = '../../../src/main/resources/lyrics/processedLyrics';
    fetch(filePath)
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! Status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            console.log('Data from JSON file:', data);
            // Now you can work with the data retrieved from the JSON file
        })
        .catch(error => {
            console.error('Error fetching data:', error);
    });
}