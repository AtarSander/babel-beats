import json
import torch
import whisper_timestamped as whisper
MODEL_SIZE = "medium"
DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")


def transcript(filepath, language, lyrics_begining):
    audio = whisper.load_audio(filepath)
    model = whisper.load_model(MODEL_SIZE, device=DEVICE)
    result = whisper.transcribe(model, audio, language=language, initial_prompt=lyrics_begining)
    return result


def generate_transcripts(songs_data):
    for song_data in songs_data:
        result = transcript(f"""../resources/audio/{song_data["name"]}.mp3""", song_data["language"], song_data["lyrics_begin"])
        with open(f"""../lyrics/timestamps/{song_data["title"]}.json""", 'w') as json_file:
            json.dump(result, json_file, ensure_ascii=False)


def load_song_data():
    with open("../resources/lyrics/songs_data.json", 'r') as file:
        loaded_data = json.load(file)
    songs_data = []
    for song in loaded_data:
        songs_data.append(song)
    return songs_data


if __name__ == "__main__":
    generate_transcripts(load_song_data())
