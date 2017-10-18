using FriendZoneAPI.Models;
using System.Collections.Generic;
using System.Linq;
using System.Data.Entity;
using System.Web.Http;

namespace FriendZoneAPI.Controllers
{
    public class PostController : ApiController
    {
        FriendZoneEntities database = new FriendZoneEntities();

        // GET api/post
        public List<Post> Get()
        {
            List<Post> posts = database.Posts.ToList();
            return posts;
        }

        // GET api/post/id
        public Post Get(int id)
        {
            Post post = database.Posts.Include(p => p.User).SingleOrDefault(p => p.ID.Equals(id));
            return post;
        }

        // GET api/post
        public List<Post> Get(int start, int count)
        {
            List<Post> posts = database.Posts.OrderByDescending(p => p.ID).ToList();
            posts.RemoveRange(0, start);
            if (count < posts.Count)
            {
                posts.RemoveRange(count, posts.Count);
            }
            return posts;
        }

        // POST api/post
        public void Post([FromBody]Post post)
        {
            database.Posts.Add(post);
            database.SaveChanges();
        }

        // PUT api/post/id
        public void Put(int id, [FromBody]Post newPost)
        {
            Post oldPost = database.Posts.Find(id);
            oldPost.Email = newPost.Email;
            oldPost.Description = newPost.Description;
            oldPost.Time = newPost.Time;
            database.SaveChanges();
        }

        // DELETE api/post/id
        public void Delete(int id)
        {
            database.Posts.Remove(database.Posts.SingleOrDefault(p => p.ID.Equals(id)));
            database.SaveChanges();
        }
    }
}
