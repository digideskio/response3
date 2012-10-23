
eventCompileStart = { type ->
    int version = "git rev-list --all".execute().text.readLines().size()
    metadata.'app.version' = "3.0.${version}".toString()
    metadata.persist()
}
