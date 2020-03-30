require 'csv'
require 'json'
require 'fileutils'

FileUtils.mkdir("calcination") unless File.directory?("calcination")
FileUtils.mkdir("dissolution") unless File.directory?("dissolution")
FileUtils.mkdir("synthesis") unless File.directory?("synthesis")

sheet = CSV.open("sheet.csv", headers:true)
sheet.each do |row|
  id = row[1]
  next if id.nil?
  filename = "#{row[1].gsub(":", "_")}.json"
  id = "minecraft:" + id unless id.include?(":")

  type = if row[2].nil? then "item" else "tag" end
  restricted = !row[3].nil?

  materia = "artofalchemy:materia_" + row[4].downcase unless row[4].nil?
  materia_amt = row[5].to_i
  materia_factor = row[19].to_f || 1.0

  essentia = {}
  for i in 6..18
    e = sheet.headers[i].downcase.prepend("artofalchemy:")
    essentia[e] = row[i].to_i unless row[i].nil?
  end
  essentia_factor = row[20].to_f || 1.0

  unless materia.nil? || materia_factor <= 0
    recipe = {}
    recipe["type"] = "artofalchemy:calcination"
    recipe["ingredient"] = { type => id }
    recipe["result"] = { "item" => materia, "count" => materia_amt.numerator }
    recipe["container"] = { "item" => "minecraft:bucket" } if id.include?("bucket")
    recipe["factor"] = materia_factor
    file = File.new("calcination/#{filename}", mode = 'w')
    file.write(JSON.generate(recipe))
  end

  unless essentia.empty? || essentia_factor <= 0
    recipe = {}
    recipe["type"] = "artofalchemy:dissolution"
    recipe["ingredient"] = { type => id }
    recipe["result"] = essentia
    recipe["container"] = { "item" => "minecraft:bucket" } if id.include?("bucket")
    recipe["factor"] = essentia_factor
    file = File.new("dissolution/#{filename}", mode = 'w')
    file.write(JSON.generate(recipe))
  end

  unless restricted || materia.nil?
    recipe = {}
    recipe["type"] = "artofalchemy:synthesis"
    recipe["target"] = { type => id }
    recipe["materia"] = { "item" => materia }
    recipe["essentia"] = essentia
    recipe["cost"] = materia_amt.ceil
    recipe["container"] = { "item" => "minecraft:bucket" } if id.include?("bucket")
    file = File.new("synthesis/#{filename}", mode = 'w')
    file.write(JSON.generate(recipe))
  end
end
