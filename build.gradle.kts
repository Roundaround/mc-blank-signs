plugins {
  id("me.roundaround.allay")
}

allay {
  displayName.set("Blank Signs")
  description.set("Place signs without opening the edit screen by sneaking.")
  authors.set(listOf("Roundaround"))
  license.set("MIT")
  homepage.set("https://modrinth.com/mod/blank-signs")
  repository.set("https://github.com/Roundaround/mc-fabric-blank-signs")
  issues.set("https://github.com/Roundaround/mc-fabric-blank-signs/issues")

  modrinth {
    projectId.set("blank-signs")
  }

  curseforge {
    projectId.set(1501496)
  }

  release {
    versionType.set("release")
    sourcesJar.set(true)
  }
}
