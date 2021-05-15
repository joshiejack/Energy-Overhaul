![](src/main/resources/assets/harvestfestival/logo.png)

Energy Overhaul is the energy usage element of harvest festival. It allows players to sleep at any time and uses energy instead of hunger. Eating food/sleeping will also restore your health. Running and jumping do not use energy by default but you can re-enable this. You will however use up energy by farming, using tools and various other activities. You won't die from a lack of energy but you will slow down. Also if you do not sleep frequently you will become tired and start blacking out!
More information about Energy Overhaul and downloads can be found on //TODO

If you have any questions, feel free to join the [Harvest Festival Discord](https://discord.gg/MRZAyze)

Adding Energy Overhaul to your buildscript
---
Add to your build.gradle:
```gradle
repositories {
  maven {
    // url of the maven that hosts energyoverhaul files
    url //TODO
  }
}

dependencies {
  // compile against Energy Overhaul
  deobfCompile "uk.joshiejack.energyoverhaul:Energy-Overhaul:${minecraft_version}-${energyoverhaul_version}"
}
```

`${minecraft_version}` & `${energyoverhaul_version}` can be found //TODO, check the file name of the version you want.