# Projekt PAP2023Z-Z04
## Członkowie zespołu
- Filip Ryniewicz
- Miłosz Cieśla
- Aleksander Szymczyk
# Opis projektu
Aplikacja pomagająca uczyć się nowych języków podczas słuchania muzyki.
Słuchając utworu zagranicznego wykonawcy, użytkownik dostaje tekst piosenki w języku oryginalnym oraz wersję przetłumaczoną na przez niego wybrany. Na podstawie historii słuchania użytkownika z serwisu Spotify, aplikacja będzie dobierać muzykę odpowiadającą jego gustom, co uczyni proces uczenia się nowego języka - przyjemnym i ekscytującym.
# Technologie
## Rozważamy dwie opcje implementacji
## Web
### Database
- MySQL
### Backend
- Spring
- Spotify API
- Genius API
- Open AI API (Whisper)
- DeepL API
### Frontend
- React
## Desktop app
### Database
- MySQL
### Fullstack
- Swing
- Spotify API
- Genius API
- Open AI API (Whisper)
- DeepL API
# Funkcjonalności
1. Przetworzenie upodobań muzycznych
- Analiza upodobań muzycznych użytkownika za pomocą Spotify API
- Dobieranie utworów z lubianych gatunków muzycznych w obcym języku
2. Synchronizacja tekstu z muzyką
- Ekstrakcja pojedynczych słów piosenki za pomocą Open AI API (Whisper) wraz z timestampami
- Synchronizacja wyświetlania/podkreślania wersów piosenki z dźwiękiem
3. Odtwarzanie piosenki w aplikacji
- Łączenie ze spotify API, uruchamianie spotify w tle
4. Wyświetlenie oryginału i przetłumaczonego tekstu w trakcie słuchania piosenki
- Pobieranie tekstu piosenki za pomocą Genius API
- Tłumaczenie tekstu na wybrany język użytkownika używając DeepL API
5. Przechowywanie przetłumaczonych i synchronizowanych tekstów
- Brak konieczności ponownych zapytań do API
