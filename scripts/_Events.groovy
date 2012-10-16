
eventCompileStart = { type ->
    int version = "git rev-list --all".execute().text.readLines().size()
    metadata.'app.version' = "${version}".toString()
    metadata.persist()
}
