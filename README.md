# OPSync
Synchronize & secure your OPs list across multiple servers. Unauthorized OPs will be removed when refreshed. The list will auto-refresh every 30 seconds, unless configured not to. All data is synchronized across every server (not exact sync, but within ~30 seconds).

**Requirements**
* None

**Setup**
* Change the database credentials in your config.yml.

**Commands**
* /opsync add <player> - Adds a player to the cache and the database.
* /opsync remove <player> - Removes a player from the cache and the database.
* /opsync refresh - Retrieves and caches a new list of OPs.

**Auto-Refresh**
* The OPs list will refresh every 30 seconds on every server. This cannot be configured for now.