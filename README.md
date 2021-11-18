# Comic browser
This is an MVP for browsing through comics in https://xkcd.com/ that contains a main page, and a BottomSheetDialog for showing a description about each comic.

This project is developed in kotlin language, it's architecture is based on clean-arch, and according to the fact that it's a simple MVP project it's just one module, also is developed in TDD.    
Here are some bullets that explain the main parts of this project more:   
- The connection between UI and logic are handled by Android-architecture-components(ViewModel, LiveData). 
- Android Navigation Components helped with navigating between Fragments, and navArgs as well for passing data.
- Retrofit is used for the API calls
- For thread-management, I used coroutines which is a lightweight framework with suitable compatibility with Kotlin.
- Koin helped with dependency injection.
- Also Glide has been used to load comics images.
- There are some unit-tests for the main logics of the ViewModel.(It's Junit based, and for mocking I've got help from mockK)

There can be some other features in the future like search through comics by their text, and getting notification when a new comic is published, also share, and favorite comics which are not considered necessary for the MVP version.
