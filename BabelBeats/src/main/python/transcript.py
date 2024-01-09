import json
import torch
import whisper_timestamped as whisper
import sys
import os.path
MODEL_SIZE = "medium"
DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")


def transcript(filepath, language):
    audio = whisper.load_audio(filepath)
    model = whisper.load_model(MODEL_SIZE, device=DEVICE)
    result = whisper.transcribe(model, audio, language=language)
    return result


def generate_transcripts(song_name, language):
    result = transcript(f"""src/main/resources/audio/{song_name}.mp3""", language)
    with open(f"""src/main/resources/lyrics/timestamps/{song_name}.json""", 'w', encoding='utf-8') as json_file:
        json.dump(result, json_file, ensure_ascii=False)


if __name__ == "__main__":
    if len(sys.argv) != 3:
        raise Exception("Script takes exactly 2 arguments")
    song_name = sys.argv[1]
    language = sys.argv[2]
    print(language)
    generate_transcripts(song_name, language)
