export function parseUtcStringAsKst(datetimeStr) {
  return new Date(datetimeStr + 'Z')
}

export function isBetweenNow(start, now, end) {
  return now >= start && now <= end
}  