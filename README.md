# PickFromHat
Pick a named note from the hat, what won't be your's and send mails for participants.

## Setup
Configure src\main\resources\settings.json

e.g.:
```
{
  "fromEmail":"ford@gmail.com",
  "fromPassword":"asdf123",
  "names":[
    {
      "email":"fairlane@gmail.com",
      "name":"MrTheBig123"
    },
    {
      "email":"tonyhawk@gmail.com",
      "name":"skate123"
    }
  ]
}
```

## Execute produced jar
- cd .../PickFromHat
- mvn clean package
- cd target
- java -jar Pick\<\<TAB>>
