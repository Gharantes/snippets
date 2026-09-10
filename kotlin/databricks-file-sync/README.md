# databricks-file-sync

Uploads a local folder's `.xml` and `.zip` files to a Databricks Unity Catalog
volume, once per run, skipping anything already uploaded. A Postgres table is
the "already uploaded" record.

Moved here from `Gharantes/sql-api` — the name was a leftover, there is no SQL
API in it.

## What's actually reusable

`src/main/kotlin/personal/dev/databricks/` is a hand-rolled client for the
Databricks Files REST API (`/api/2.0/fs`), built on Ktor rather than the
official SDK. Six calls, one class each, all behind `DatabricksService`:

```kotlin
databricksService.createDirectory("/Volumes/main/default/my_volume/incoming")
databricksService.uploadFile("/Volumes/main/default/my_volume/incoming/a.xml", File("a.xml"))
databricksService.getDirectoryContent("/Volumes/main/default/my_volume/incoming")
databricksService.downloadFile("/Volumes/main/default/my_volume/incoming/a.xml")
databricksService.deleteFile("/Volumes/main/default/my_volume/incoming/a.xml")
databricksService.deleteDirectory("/Volumes/main/default/my_volume/incoming")
```

Every call is `runBlocking` around a suspend body — `IApi.execute` is
synchronous by design, so callers never see a coroutine. `useClient` builds and
closes a fresh `HttpClient` per call: simple, and fine at one-run-per-invocation
scale, wasteful in a loop.

Upload sends `overwrite=false`, so re-uploading the same path fails at the API
rather than silently replacing. That is why the Postgres bookkeeping exists.

## The sync loop

`FileSyncService.syncFiles()` is the app-specific half:

1. Insert a row in `sync_history`, get its id.
2. For each non-directory `.xml`/`.zip` in `sync-folder`:
   - not in `file_history` → upload, then insert with `added_at = syncHistoryId`
   - already there → bump `last_validated_at = syncHistoryId`

So `file_history.last_validated_at` tells you which run last saw a file, which
is how you spot files that disappeared from the source folder.

## Running it

1. Copy `src/main/resources/application-dev.yaml` to `application.yaml` (same
   folder). The `-dev` one is the template and is the only one committed.
2. Fill in `instance-url`, `token`, `sync-folder`, `volume`, `folder` and the
   `spring.datasource` block. `docs/` walks through each — in Portuguese.
3. Create the two tables from [`docs/02-datasource.md`](docs/02-datasource.md).
4. `gradle bootRun` — there is no wrapper here, use your own Gradle (needs JDK 21).

It runs as a `CommandLineRunner` and calls `exitProcess(0)`, so it is a one-shot
job, not a server. The `server.port: 8090` in the yaml is dead config, no web
starter is on the classpath.

## Known gaps

- `docs/04-sync-folder.md` is empty and `docs/05-volume-e-folder.md` stops
  mid-sentence. The volume has to be created by hand in the Databricks UI.
- `insertFileHistory` and `updateValidation` interpolate `syncHistoryId` and
  `id` straight into the SQL string instead of binding them. They are `Long`s
  read back from the database, so not injectable here, but the binding is
  already set up one line away.
- `IRequest` and `IResponse` are empty marker interfaces, only there to
  constrain `IApi`'s type parameters.
- The list response carries `next_page_token`, but nothing pages on it. One
  request per directory, so a directory past the API's page size comes back
  truncated.
- `ApiCatalogListDirectoryContentsService` decodes with a bare
  `Json.decodeFromString`, not the `ignoreUnknownKeys = true` instance that
  `useClient` installs for content negotiation. A new field in the Databricks
  response throws there.

## Databricks-side notebook — [`databricks/`](databricks)

Unrelated to the Kotlin app, and the reason the uploaded files are `.xml`/`.zip`:
`index.py` reads Brazilian NF-e XML out of the uploaded zips with BeautifulSoup
and flattens it into a pandas DataFrame, classifying each document as `NFE`,
`CANC` or `EVENTO`. `0-node.txt` is the first cell (`%pip install` lines).
