# MySpellBuddy
Android application for automatic dictation verification using Google's ML Kit

## Motivation
Dictation as a transcription of the spoken text is a common way of checking spelling in schools
and is used to check the spelling of the Croatian language, as well as foreign languages ​​that students have entered.
The purpose of this application is to enable independent exercise
dictation in different languages ​​using automatic recognition of written text in a simple and accessible way.

## User requests
Audio and text recordings of texts to be used for dictation will be stored on the Firebase platform.
The student begins his practice by scanning the code that uniquely determines the text to be dictated, after which the
the mobile application plays the sound track of the text.
Depending on the settings, enable repeat and pause playback.
The student can write the text on paper, so the application must be able to take pictures of the written text,
or he can write it using a pen on mobile devices (tablets) provided for this purpose.
The recognized text must be able to be compared with the dictated text and errors should be shown to the student.

## Technologies
To scan the code, recognize text from images and recognize digitally written text, tools from
Google's development kit ML Kit.
The application was written in the Kotlin programming language, and Firebase's Storage was used to store data.
