#!/usr/bin/env ruby
#
# batch.rb - Processes a queue of media files for transcoding
# 
# This script reads file paths from queue.txt and processes them one by one,
# calling the external transcoder with the arguments passed to this script.
#

QUEUE_PATH = 'queue.txt'

while true
  # Read the queue file
  content = File.read(QUEUE_PATH)
  input = content.match(/^.*\R/).to_s.chomp

  # Exit if queue is empty
  break if input.empty?

  # Remove the processed item from the queue
  queue = File.new(QUEUE_PATH, 'wb')
  queue.print content.sub(/^.*\R/, '')
  queue.close

  # Process the file and break if there's an error
  puts "Processing: #{input}"
  break unless system('other-transcode', *ARGV, input)
end