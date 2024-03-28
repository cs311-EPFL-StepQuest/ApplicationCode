# ApplicationCode
Figma project : https://www.figma.com/file/r8L5v1avlD0mruSlr6Akvt/StepQuest---Figma?type=design&node-id=0%3A1&mode=design&t=WWJ6baSKShap6a4s-1

### Map
Add Google map api key to local.properties file
```
MAPS_API_KEY=YOUR_API_KEY
```

TODO: need to move line "navigationActions.navigateTo(TopLevelDestination(Route.MAP))" to where it should be after main page is ready, now is inside onSignInResult (when sign in it will directly go to Map page)
