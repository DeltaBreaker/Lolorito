# Lolorito
Lolorito is a tool for sifting though market data in FFXIV **(requires Java 1.8)**

It takes the item data from [XIVAPI](https://github.com/xivapi/ffxiv-datamining) and uses the market data from [Universalis](https://universalis.app/) to find the current most profitable items to sell on the market. It takes into account both how much items cost and how quickly they sell. Sometimes a larger quantity of a lower cost item can out perform a lot of high-price items that rarely sell.

## Getting Started
First just select what server you want to pull market data for, pick how far back you want the data to go, press update, and wait for it to finish updating.

When its done you can pick what categories to search in and any search term and sort type you'd like to use.

Finally just press search and the results will appar on the right. You can search as many times as you'd like. Updating the market data only needs to be done once.

![image](https://user-images.githubusercontent.com/6527156/188293104-4986077c-2f57-426d-b1c6-4a3e92ba79f6.png)

### Crafting Trees
If an item can be crafted, the "Crafting Profit" column will show the highest profit you can make from buying the required materials and crafting the item. Clicking on that row will open a crafting tree showing what items are needed and if it costs less to make the materials yourself or just buy them from the market. It shows the profits for both high and low qualities. The required materials show the lower of the two when calculating costs and takes into account how many are needed as well. Lastly, you can set items as obtained so that they arent factored into the crafting cost.

![image](https://user-images.githubusercontent.com/6527156/188293098-4e8b4a5c-3baf-44f3-aaee-733ab5b45900.png)

### Material List

The material list shows what items aren't factored into crafting costs for other items. This can be used to mark items that you can easily gather yourself, such as shards and crystals, or anything else you already have on hand. The active items are listed on the far right while the middle column displays a list of items that can be set or removed as obtained. Items are shown here based on the term entered into the search box.

![image](https://user-images.githubusercontent.com/6527156/188293247-22771170-d45b-496b-8a90-e808209dd98f.png)

## Downloads
See the "Releases" page [here](https://github.com/DeltaBreaker/Lolorito/releases)
