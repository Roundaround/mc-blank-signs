plugins {
  id("me.roundaround.allay")
}

allay {
  displayName.set("Blank Signs")
  description.set("Place signs without opening the edit screen by sneaking.")
  authors.set(listOf("Roundaround"))
  license.set("MIT")
  homepage.set("https://modrinth.com/mod/blank-signs")
  repository.set("https://github.com/Roundaround/mc-blank-signs")
  issues.set("https://github.com/Roundaround/mc-blank-signs/issues")
  logoFile.set("assets/blanksigns/banner.png")

  gametest {
    // Acknowledge the Minecraft EULA for the throwaway worlds the headless
    // server game test spins up.
    eula.set(true)
  }

  modrinth {
    projectId.set("blank-signs")
  }

  curseforge {
    projectId.set(1501496)
  }

  release {
    versionType.set("release")
    minecraftVersions("26.2")
    changelogDir.set(file("changelogs"))
  }
}
