import json
import torch
import whisper_timestamped as whisper
import sys
import os
import subprocess

MODEL_SIZE = "medium"
DEVICE = torch.device("cuda:0" if torch.cuda.is_available() else "cpu")

def separate_vocals(song_name):
    command = [
    "python",
    "-m",
    "demucs",
    "--mp3",
    "--two-stems=vocals",
    f"src/main/resources/audio/{song_name}.mp3",
    "-o",
    "src/main/resources/audio/"
    ]
    
    try:
        process = subprocess.Popen(command)
        process.wait()
        exit_code = process.returncode
        print("Exit Code:", exit_code)

    except Exception as e:
        print(e)

    return exit_code

def transcript(filepath, language):
    print("whisper working")
    audio = whisper.load_audio(filepath)
    model = whisper.load_model(MODEL_SIZE, device=DEVICE)
    result = whisper.transcribe(model, audio, language=language)
    return result


def generate_transcripts(song_name, language):
    separate_vocals(song_name)
    result = transcript(f"""src/main/resources/audio/htdemucs/{song_name}/vocals.mp3""", language)
    try:
        os.remove(f"""src/main/resources/audio/htdemucs/{song_name}""")
    except OSError as e:
        print(f"Error deleting audio file: {e}")
    with open(f"""src/main/resources/lyrics/timestamps/{song_name}.json""", 'w', encoding='utf-8') as json_file:
        json.dump(result, json_file, ensure_ascii=False)


if __name__ == "__main__":
    if len(sys.argv) != 3:
        raise Exception("Script takes exactly 2 arguments")
    song_name = sys.argv[1]
    language = sys.argv[2]
    generate_transcripts(song_name, language)
