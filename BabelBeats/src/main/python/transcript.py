import json
import torch
import whisper_timestamped as whisper
import sys
MODEL_SIZE = "medium"
DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")


def transcript(filepath, language, lyrics_beginning):
    audio = whisper.load_audio(filepath)
    model = whisper.load_model(MODEL_SIZE, device=DEVICE)
    result = whisper.transcribe(model, audio, language=language, initial_prompt=lyrics_beginning)
    return result


def generate_transcripts(songs_data, song_name):
    for song_data in songs_data:
        if song_name == songs_data["title"]:
            result = transcript(f"""../resources/audio/{song_data["title"]}.mp3""", song_data["language"], song_data["lyrics"].split(" ")[:25])
            with open(f"""../lyrics/timestamps/{song_data["title"]}.json""", 'w') as json_file:
                json.dump(result, json_file, ensure_ascii=False)
            break


def load_song_data():
    with open("../resources/lyrics/songs_data.json", 'r') as file:
        loaded_data = json.load(file)
    songs_data = []
    for song in loaded_data:
        songs_data.append(song)
    return songs_data


if __name__ == "__main__":
    if len(sys.argv) != 1:
        raise Exception("Script takes exactly 1 argument")
    song_name = sys.argv[1]
    generate_transcripts(load_song_data(), song_name)
