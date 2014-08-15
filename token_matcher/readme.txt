Problem :

A message broker needs a way to find all subscribers listening to a destination (including hierarchical subscriptions).
A client should be able to subscribe to multiple topics relevant to a given context with a single subscription using this pattern.
e.g. To capture all news for all categories for today - can subscribe like "news.+.today".

This class provides an efficient, abstract way to manage such subscriptions. (inspired by moquette subscription logic)

Hierarchy definitions :
	dest.* - captures all immediate child elements to "dest".
	dest.# - captures all immediate child elements + parent (e:g: "dest" and "dest.news")
	dest.news - captures exactly "dest.news" destination
	dest.+.news - captures "dest.sports.news" and "dest.crisis.news"