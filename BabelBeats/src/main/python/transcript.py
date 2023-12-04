import json
import torch
import whisper_timestamped as whisper
MODEL_SIZE = "medium"
DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")


def transcript(filepath, language, lyrics_beggining):
    audio = whisper.load_audio(filepath)
    model = whisper.load_model(MODEL_SIZE, device=DEVICE)
    result = whisper.transcribe(model, audio, language=language, initial_prompt=lyrics_beggining)
    return result


def generate_transcripts(songs_data):
    for song_data in songs_data:
        result = transcript(f"""lyrics/audio/{song_data["title"]}.mp3""", song_data["language"], song_data["lyrics_begin"])
        with open(f"""lyrics/transcripts/{song_data["title"]}.json""", 'w') as json_file:
            json.dump(result, json_file, ensure_ascii=False)


def load_song_data():
    with open("lyrics/songs_data.json", 'r') as file:
        loaded_data = json.load(file)
    songs_data = []
    for song in loaded_data:
        songs_data.append(song)
    return songs_data


if __name__ == "__main__":
    songs_data = load_song_data()
    generate_transcripts(songs_data)
