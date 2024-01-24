# from deep_translator import DeeplTranslator
import deepl
import json


def readData(fileName):
    wordsList = []
    with open(fileName) as fh:
        for word in fh:
            wordsList.append(word.strip("\n"))
    return wordsList


def readKeysJSON(fileName):
    with open(fileName) as fh:
        data = json.load(fh)
    return data


def translateWords(wordsList, languageDict):
    translatedList = [{"English": word} for word in wordsList]
    translator = deepl.Translator("2ed63044-b0be-21d0-dd75-4f4b57d94c37:fx")
    for key, value in languageDict.items():
        translated = translator.translate_text(
            wordsList, target_lang=value, source_lang="en"
        )
        for i in range(len(wordsList)):
            translatedList[i][key] = translated[i].text

    return translatedList


def saveToJson(fileName, jsonData):
    with open(fileName, "w") as file:
        json.dump(jsonData, file, indent=2)


if __name__ == "__main__":
    wordsList = readData("popularWords.csv")
    languageDict = readKeysJSON("languageMap.json")
    translatedJSON = translateWords(wordsList, languageDict)
    saveToJson("flashCards.json", translatedJSON)
